package com.zhy.apiclientsdk.model.request;


import com.zhy.apiclientsdk.model.enums.RequestMethodEnum;
import com.zhy.apiclientsdk.model.params.NameParams;
import com.zhy.apiclientsdk.model.response.NameResponse;
import lombok.experimental.Accessors;


@Accessors(chain = true)
public class NameRequest extends BaseRequest<NameParams, NameResponse> {

    @Override
    public String getPath() {
        return "/name";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link NameResponse}>
     */
    @Override
    public Class<NameResponse> getResponseClass() {
        return NameResponse.class;
    }


    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }
}
