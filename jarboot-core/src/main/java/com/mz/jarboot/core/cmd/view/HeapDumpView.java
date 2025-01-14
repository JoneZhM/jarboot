package com.mz.jarboot.core.cmd.view;

import com.alibaba.fastjson.JSON;
import com.mz.jarboot.core.cmd.model.HeapDumpModel;

public class HeapDumpView implements ResultView<com.mz.jarboot.core.cmd.model.HeapDumpModel> {
    @Override
    public String render(HeapDumpModel model) {
        return JSON.toJSONString(model);
    }

    @Override
    public boolean isJson() {
        return true;
    }
}
