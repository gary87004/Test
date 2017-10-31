public class Api {

    public enum Action {
        ANNOUNCEMENT,
        ANNOUNCEMENT_GET,
        ANNOUNCEMENT_PHOTO,
        BCN_ALL_GET,
        BCN_GET,
        BCN_UPDATE,
        CALENDAR_GET,
        CALENDAR_CREATE,
        CLASSES_ALL_GET,
        CLASS_CREATE,
        CLASS_DAILY_ROLLCALL_RECORDS_GET,
        CLASS_UPDATE,
        DEVICE_REGISTER,
        LOCATION_CREATE,
        LOCATION_GET,
        LOCATION_GET_MULTIPLE_LOCATIONS,
        LOCATION_LOCATORS,
        LOCATION_UPDATE,
        NOTIFICATION_GET,
        PARENT_FETCH_ALL,
        PROMOTION_CREATE,
        PROMOTION_IMAGE_UPLOAD,
        SUB_LOCATIONS_GET,
        TRANSIT_IOT_POST,
        USER_CHECK_IN,
        USER_SIGN_IN,
        USER_SIGN_OUT
    }

    private final static ImmutableMap<Action, String> ACTION_URL_MAP = ImmutableMap.<Action, String>builder()
            .put(Action.USER_SIGN_IN, "/api/user/signin")
            .put(Action.USER_SIGN_OUT, "/api/user/signout")
            .put(Action.LOCATION_GET, "/api/loc")
            .put(Action.LOCATION_GET_MULTIPLE_LOCATIONS, "/api/loc/locs")
            .put(Action.LOCATION_UPDATE, "/api/loc")
            .put(Action.LOCATION_CREATE, "/api/loc")
            .put(Action.SUB_LOCATIONS_GET, "/api/loc/sublocs")
            .put(Action.BCN_ALL_GET, "/api/bcn/all")
            .put(Action.ANNOUNCEMENT_GET, "/api/vas/anncs")
            .put(Action.ANNOUNCEMENT, "/api/vas/annc")
            .put(Action.ANNOUNCEMENT_PHOTO, "/api/vas/annc/photo")
            .put(Action.DEVICE_REGISTER, "/api/device/register")
            .put(Action.NOTIFICATION_GET, "/api/notification")
            .put(Action.CLASSES_ALL_GET, "/api/class/all")
            .put(Action.CLASS_DAILY_ROLLCALL_RECORDS_GET, "/api/class/daily_form")
            .put(Action.CALENDAR_GET, "/api/vas/cals")
            .put(Action.CALENDAR_CREATE, "/api/vas/cal")
            .put(Action.TRANSIT_IOT_POST, "/api/bcn/transit/teacher")
            .put(Action.PARENT_FETCH_ALL, "/api/parent/fetch/all")
            .put(Action.CLASS_CREATE, "/api/class")
            .put(Action.BCN_GET, "/api/bcn")
            .put(Action.CLASS_UPDATE, "/api/class")
            .put(Action.BCN_UPDATE, "/api/bcn")
            .put(Action.LOCATION_LOCATORS, "/api/loc/locators")
            .put(Action.USER_CHECK_IN, "/api/user/check_in")
            .put(Action.PROMOTION_CREATE, "/api/promotion")
            .put(Action.PROMOTION_IMAGE_UPLOAD, "/api/promotion/loc/img")
            .build();

    public void postRequest(Action action, final Map<String, String> postParams, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);
        Helper.printDebugInfo(url);
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return postParams;
            }
        };

        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(stringPostRequest);
    }

    public void postRequestWithAuthorization(Action action, final Map postParams, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map getParams() throws AuthFailureError {
                return postParams;
            }
        };
        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(stringPostRequest);
    }

    public void getRequestWithAuthorization(Action action, @Nullable final Map params, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);

        // 若有夾帶參數，將參數Map轉換為字串接再網址後面
        if (params != null) {
            url += "?" + getParamsToUrl(params);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(stringRequest);
    }

    public void getRequestWithAuthorizationByJsonObject(Action action, @Nullable final Map params, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);

        // 若有夾帶參數，將參數Map轉換為字串接再網址後面
        if (params != null) {
            url += "?" + getParamsToUrl(params);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return com.aengin.rollcall.common.network.Api.getAuthorizationHeader();
            }


        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 180000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mVolleyRequestQueue.add(jsonObjectRequest);
    }

    public void putRequestWithAuthorization(Action action, @Nullable final Map params, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);

        // 若有夾帶參數，將參數Map轉換為字串接再網址後面
        if (params != null) {
            url += "?" + getParamsToUrl(params);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(stringRequest);
    }

    public void deleteRequestWithAuthorization(Action action, @Nullable final Map params, Listener successListener, ErrorListener errorListener) {
        String url = Config.API_HOST + ACTION_URL_MAP.get(action);

        // 若有夾帶參數，將參數Map轉換為字串接再網址後面
        if (params != null) {
            url += "?" + getParamsToUrl(params);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(stringRequest);
    }

    public void putMultipartRequestWithAuthorization(Action action, final Map<String, String> putParams, @Nullable final Map<String, DataPart> fileData, Listener successListener, ErrorListener errorListener) {
        String postRequestApi = Config.API_HOST + ACTION_URL_MAP.get(action);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, postRequestApi, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return putParams;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                if (fileData == null) {
                    return super.getByteData();
                } else {
                    return fileData;
                }
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(multipartRequest);
    }

    public void postPhotoRequestWithAuthorization(Action action, Object tag, final Map<String, String> putParams, @Nullable final Map<String, DataPart> fileData, Listener successListener, ErrorListener errorListener) {
        String postRequestApi = Config.API_HOST + ACTION_URL_MAP.get(action);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, postRequestApi, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return putParams;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                if (fileData == null) {
                    return super.getByteData();
                } else {
                    return fileData;
                }
            }


        };
        if (tag != null)
            multipartRequest.setTag(tag);

        multipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                // TODO: Please declare it as a final static variable.
                return 180000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mVolleyRequestQueue.add(multipartRequest);
    }

    public void postMultipartRequestWithAuthorization(Action action, final Map<String, String> putParams, @Nullable final Map<String, DataPart> fileData, Listener successListener, ErrorListener errorListener) {
        String postRequestApi = Config.API_HOST + ACTION_URL_MAP.get(action);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, postRequestApi, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Api.getAuthorizationHeader();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return putParams;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                if (fileData == null) {
                    return super.getByteData();
                } else {
                    return fileData;
                }
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyRequestQueue.add(multipartRequest);
    }

    public String getParamsToUrl(Map<?, ?> getParams) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<?, ?> entry : getParams.entrySet()) {
             if (stringBuilder.length() > 0) {
                 stringBuilder.append("&");
             }
            stringBuilder.append(String.format("%s=%s", entry.getKey().toString(), entry.getValue().toString()));
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> getAuthorizationHeader() {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.shareInstance(mContext);
        String userToken = sharedPreferencesManager.getString(SharedPreferencesManager.Key.USER_TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("authorization", "Bearer " + userToken);
        Helper.printDebugInfo("authorization: " + "Bearer " + userToken);
        return map;
    }

}

