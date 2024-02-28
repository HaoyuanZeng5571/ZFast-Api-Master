package com.zhy.apiclientsdk.model.request;

import com.zhy.apiclientsdk.model.enums.RequestMethodEnum;
import com.zhy.apiclientsdk.model.params.PoisonousChickenSoupParams;
import com.zhy.apiclientsdk.model.response.PoisonousChickenSoupResponse;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class PoisonousChickenSoupRequest extends BaseRequest<PoisonousChickenSoupParams, PoisonousChickenSoupResponse>{
    @Override
    public String getPath() {
        return "/poisonousChickenSoup";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link PoisonousChickenSoupResponse}>
     */
    @Override
    public Class<PoisonousChickenSoupResponse> getResponseClass() {
        return PoisonousChickenSoupResponse.class;
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }
}
