package com.tinyblack.framework.clean.base;



import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yubiao
 */

public class UseCaseControl implements ResControl {
    private List<UseCase> data;

    @Override
    public boolean canDeal(ResObject t) {
        if (t == null) {
            return false;
        }
        if (t.getResource() == null) {
            return false;
        }
        if (t.getResource() instanceof Class) {
            return t.getResource() == UseCase.class;
        }
        if (t.getResource() instanceof UseCase) {
            return true;
        }
        return false;
    }


    @Override
    public void addResource(ResObject useCase) {
        if (useCase == null) {
            return;
        }
        if (useCase.getResource() instanceof UseCase) {
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add((UseCase) useCase.getResource());
        }

    }

    @Override
    public void releaseResource() {
        if (data != null) {
            for (UseCase useCase : data) {
                if (useCase != null) {
                    useCase.release();
                }
            }
        }
    }

    @Override
    public void releaseResource(ResObject resObject) {

    }
}
