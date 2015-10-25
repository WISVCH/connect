package ch.wisv.connect.client;

import ch.wisv.connect.common.model.CHUserInfo;
import com.google.gson.JsonObject;
import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.UserInfo;

/**
 * CH user info fetcher.
 */
public class CHUserInfoFetcher extends UserInfoFetcher {
    @Override
    protected UserInfo fromJson(JsonObject userInfoJson) {
        return CHUserInfo.fromJson(userInfoJson);
    }
}
