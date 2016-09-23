package com.alorma.github.sdk.services.client;

import com.alorma.github.sdk.security.InterceptingListOkClient;
import com.alorma.gitskarios.core.client.BaseListClient;
import com.alorma.gitskarios.core.client.LogProvider;
import com.alorma.gitskarios.core.client.UrlProvider;
import com.squareup.okhttp.OkHttpClient;
import core.ApiClient;
import core.Github;
import core.GithubEnterprise;

public abstract class GithubListClient<K> extends BaseListClient<K> {

  public GithubListClient() {
    super(getApiClient());
  }

  private static ApiClient getApiClient() {
    if (UrlProvider.getInstance() == null) {
      return new Github();
    } else {
      String url = UrlProvider.getInstance().getUrl();
      return new GithubEnterprise(url);
    }
  }

  @Override
  public void intercept(RequestFacade request) {
    request.addHeader("Accept", getAcceptHeader());
    request.addHeader("User-Agent", "Gitskarios");
    if (getToken() != null && getToken().length() > 0) {
      request.addHeader("Authorization", "token " + getToken());
    }
  }

  @Override
  public void log(String message) {
    if (LogProvider.getInstance() != null) {
      LogProvider.getInstance().log(message);
    }
  }

  public String getAcceptHeader() {
    return "application/vnd.github.v3.json";
  }

  @Override
  protected InterceptingListOkClient getInterceptor() {
    return new InterceptingListOkClient(new OkHttpClient(), this);
  }
}
