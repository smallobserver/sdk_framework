package com.tinyblack.framework.net.interceptor;

import androidx.annotation.NonNull;

import com.tinyblack.framework.log.TLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a
 * {@linkplain OkHttpClient#networkInterceptors() network interceptor}.
 * <p/>
 * The format of the logs created by this class should not be considered stable and may change
 * slightly between releases. If you need a stable logging format, use your own interceptor.
 *
 * @author yubiao
 */
public class HttpLoggingInterceptor extends CommonInterceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p/>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1 (3-byte body)
         *
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p/>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Url: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p/>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Url: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        /**
         * 打印日志
         *
         * @param message
         */
        void log(String message);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                logs("HttpLog", message);
            }

            public void logs(String tag, String msg) {  //信息太长,分段打印
                //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
                //  把4*1024的MAX字节打印长度改为2001字符数
//                int max_str_length = 2001 - tag.length();
//                //大于4000时
//                while (msg.length() > max_str_length) {
//                    LogUtils.logger("d", tag, msg.substring(0, max_str_length));
//                    msg = msg.substring(max_str_length);
//                }
                //剩余部分
//                LogUtils.logger("d", tag, msg);
                TLog.d("\t\t" + tag + " -> " + msg);
            }
        };
    }

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }


    public HttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    @NonNull
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        StringBuffer requestLogMessage = new StringBuffer();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> " + request.method() + "\t" + request.url() + "\t" + protocol(protocol);

        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        requestLogMessage.append("\t\t");
        requestLogMessage.append(requestStartMessage);
        requestLogMessage.append("\n");

        if (logHeaders) {
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                requestLogMessage.append("\n");
                requestLogMessage.append("\t\t");
                requestLogMessage.append(headers.name(i)).append(": ").append(headers.value(i));
            }
            String endMessage = "--> END " + request.method();
            if (logBody && hasRequestBody) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(charset);
                }

                requestLogMessage.append("\n");
                requestLogMessage.append("\t\t");
                requestLogMessage.append(buffer.readString(charset));

                endMessage += " (" + requestBody.contentLength() + "-byte body)";
            }
            requestLogMessage.append("\n");
            requestLogMessage.append("\n");
            requestLogMessage.append(endMessage);

        }

        long startNs = System.nanoTime();
        okhttp3.Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        requestLogMessage.append("\n")
                .append("\n")
                .append("<-- ")
                .append(response.request().url())
                .append("\n")
                .append(protocol(response.protocol()))
                .append("\t").
                append(response.code())
                .append("\t")
                .append(response.message())
                .append(" (")
                .append(tookMs)
                .append("ms")
                .append(!logHeaders ? ", " + Objects.requireNonNull(responseBody).contentLength() + "-byte body" : "")
                .append(')');


        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                requestLogMessage.append("\n")
                        .append(headers.name(i))
                        .append(": ")
                        .append(headers.value(i));
            }

            String endMessage = "<-- END HTTP";
            if (logBody) {
                assert responseBody != null;
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }

                if (responseBody.contentLength() != 0) {
                    requestLogMessage.append("\n");
                    assert charset != null;
                    requestLogMessage.append("\n")
                            .append("\t\t")
                            .append(buffer.clone().readString(charset));
                }

                endMessage += " (" + buffer.size() + "-byte body)";
            }
            requestLogMessage.append("\n")
                    .append("\n")
                    .append(endMessage);
        }
        logger.log(requestLogMessage.toString());
        return response;
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }


}

