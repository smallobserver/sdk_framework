package com.tinyblack.framework.clean.base;



import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yubiao
 */

public class UseCaseGroupControl implements ResControl {
    private List<UseCaseGroup> data;

    @Override
    public boolean canDeal(ResObject t) {
        if (t == null) {
            return false;
        }
        if (t.getResource() == null) {
            return false;
        }
        if (t.getResource() instanceof Class) {
            return t.getResource() == UseCaseGroup.class;
        }
        if (t.getResource() instanceof UseCaseGroup) {
            return true;
        }
        return false;
    }

    @Override
    public void addResource(ResObject useCase) {
        if (useCase.getResource() instanceof UseCaseGroup) {
            if (data == null) {
                data = new ArrayList<>();
            }
            data.add((UseCaseGroup) useCase.getResource());
        }

    }

    @Override
    public void releaseResource() {
        if (data != null) {
            for (UseCaseGroup useCase : data) {
                if (useCase != null) {
                    useCase.release();
                }
            }
        }
    }


    @Override
    public void releaseResource(ResObject o) {

    }
}
