package com.taobao.api.internal.util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.CipherSuite;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.TlsVersion;
import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoCallback;
import com.taobao.api.TaobaoParser;
import com.taobao.api.TaobaoResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 网络工具类。
 *
 * @author carver.gu
 * @since 1.0, Sep 12, 2009
 */
public abstract class WebUtils {

    private static final String DEFAULT_CHARSET = Constants.CHARSET_UTF8;
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    //	private static final Certificate verisign; // 淘宝根证书
    private static boolean ignoreSSLCheck; // 忽略SSL检查
    private static final OkHttpClient httpClient;

    static {
        List<ConnectionSpec> specs = new ArrayList<ConnectionSpec>();
        specs.add(new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build());
        specs.add(new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build());
        httpClient = new OkHttpClient();
        httpClient.setConnectionSpecs(specs);
        httpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        httpClient.setReadTimeout(15, TimeUnit.SECONDS);
        /**
         InputStream input = null;
         try {
         CertificateFactory cf = CertificateFactory.getInstance("X.509");
         input = WebUtils.class.getResourceAsStream("/verisign.crt");
         verisign = cf.generateCertificate(input);

         } catch (Exception e) {
         throw new RuntimeException(e);
         } finally {
         if (input != null) {
         try {
         input.close();
         } catch (IOException e) {
         }
         }
         }
         */
    }

    public static class VerisignTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            /**
             * X509Certificate taobao = null; for (X509Certificate cert : chain)
             * { cert.checkValidity(); // 验证证书是否已经过期 try { String dn =
             * cert.getSubjectX500Principal().getName(); LdapName ldapDN = new
             * LdapName(dn); for (Rdn rdn : ldapDN.getRdns()) { if
             * ("CN".equals(rdn.getType()) &&
             * "*.taobao.com".equals(rdn.getValue())) { taobao = cert; //
             * 查询是否存在淘宝网的证书 break; } } } catch (Exception e) { throw new
             * CertificateException(e); } } if (taobao != null) { try {
             * taobao.verify(verisign.getPublicKey()); // 验证证书是否是权威机构颁发的 } catch
             * (Exception e) { throw new CertificateException(e); } } else {
             * throw new
             * CertificateException("Taobao.com certificate not exists!"); }
             */
        }
    }

    public static class TrustAllTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }

    private WebUtils() {
    }

    public static void setIgnoreSSLCheck(boolean ignoreSSLCheck) {
        WebUtils.ignoreSSLCheck = ignoreSSLCheck;
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应字符串
     */
    public static String doPost(String url, Map<String, String> params,
                                int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @return 响应字符串
     */
    public static String doPost(String url, Map<String, String> params,
                                String charset, int connectTimeout, int readTimeout)
            throws IOException {
        return doPost(url, params, charset, connectTimeout, readTimeout, null);
    }

    /**
     * static class HttpCallback <T extends TaobaoResponse> implements Callback
     * { Handler mHandler ; TaobaoParser<T> parser; RequestParametersHolder
     * requestHolder; TaobaoCallback<T> callback;
     * <p>
     * public HttpCallback(TaobaoParser<T> parser2, RequestParametersHolder
     * requestHolder, TaobaoCallback<T> callback2) { super(); this.parser =
     * parser2; this.requestHolder = requestHolder; this.callback = callback2; }
     * <p>
     * public HttpCallback() { }
     *
     * @Override public void onFailure(Request arg0, IOException arg1) {
     * Looper.prepare(); mHandler = new Handler(){ public void
     * handleMessage(Message msg){ Looper.myLooper().quit(); } };
     * <p>
     * <p>
     * callback.onError(null, "http error");
     * <p>
     * mHandler.sendEmptyMessage(0); //send ourself a message so the
     * looper can stop itself Looper.loop();
     * <p>
     * }
     * @Override public void onResponse(Response httpResponse) throws
     * IOException { Looper.prepare(); mHandler = new Handler(){
     * public void handleMessage(Message msg){
     * Looper.myLooper().quit(); } }; // TODO Auto-generated method
     * stub
     * <p>
     * <p>
     * <p>
     * if (!httpResponse.isSuccessful()) { callback.onError(null,
     * "httpResponse error"); }
     * <p>
     * String httpRspBody = httpResponse.body().string();
     * requestHolder.setResponseBody(httpRspBody); T tRsp = null;
     * <p>
     * try { tRsp = parser.parse(requestHolder.getResponseBody());
     * tRsp.setBody(requestHolder.getResponseBody()); } catch
     * (RuntimeException e) { TaobaoLogger.logBizError(requestHolder
     * .getResponseBody()); callback.onError(null, e.getMessage()); }
     * catch (ApiException e) { TaobaoLogger.logBizError(requestHolder
     * .getResponseBody()); callback.onError(null, e.getMessage()); }
     * <p>
     * tRsp.setParams(requestHolder.getApplicationParams()); if
     * (!tRsp.isSuccess()) { TaobaoLogger.logErrorScene(requestHolder,
     * tRsp, ClientInfo.getAppSecret()); }
     * <p>
     * callback.onSuccess(tRsp);
     * <p>
     * mHandler.sendEmptyMessage(0); //send ourself a message so the
     * looper can stop itself Looper.loop(); }
     * <p>
     * }
     */
    public static <T extends TaobaoResponse> void doPostAsync(String url,
                                                              Map<String, String> params, String charset, int connectTimeout,
                                                              int readTimeout, Map<String, String> headerMap,
                                                              final TaobaoParser<T> parser,
                                                              final RequestParametersHolder requestHolder,
                                                              final TaobaoCallback<T> callback) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        // Callback httpCallback = new HttpCallback(parser, requestHolder,
        // callback);
        // _doPostA(url, ctype, query, connectTimeout, readTimeout, headerMap,
        // httpCallback);

        _doPostA(url, ctype, query, connectTimeout, readTimeout, headerMap,
                new Callback() {

                    @Override
                    public void onResponse(Response httpResponse)
                            throws IOException {
                        if (!httpResponse.isSuccessful()) {
                            callback.onError(null, "httpResponse error");
                            return;
                        }

                        String httpRspBody = httpResponse.body().string();
                        requestHolder.setResponseBody(httpRspBody);
                        T tRsp = null;

                        try {
                            tRsp = parser.parse(requestHolder.getResponseBody());
                            tRsp.setBody(requestHolder.getResponseBody());
                        } catch (RuntimeException e) {
                            TaobaoLogger.logBizError(requestHolder
                                    .getResponseBody());
                            callback.onError(null, e.getMessage());
                            return;
                        } catch (ApiException e) {
                            TaobaoLogger.logBizError(requestHolder
                                    .getResponseBody());
                            callback.onError(null, e.getMessage());
                            return;
                        }

                        tRsp.setParams(requestHolder.getApplicationParams());
                        if (!tRsp.isSuccess()) {
                            TaobaoLogger.logErrorScene(requestHolder, tRsp,
                                    "");
                        } else {
                            LogUtils.d("top result", tRsp.getBody());
                        }

                        callback.onSuccess(tRsp);

                    }

                    @Override
                    public void onFailure(Request arg0, IOException arg1) {
                        callback.onError(null, "http error");

                    }
                });

    }

    public static String doPost(String url, Map<String, String> params,
                                String charset, int connectTimeout, int readTimeout,
                                Map<String, String> headerMap) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(charset);
        }
        return _doPost(url, ctype, content, connectTimeout, readTimeout,
                headerMap);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url     请求地址
     * @param ctype   请求类型
     * @param content 请求字节数组
     * @return 响应字符串
     */
    public static String doPost(String url, String ctype, byte[] content,
                                int connectTimeout, int readTimeout) throws IOException {
        return _doPost(url, ctype, content, connectTimeout, readTimeout, null);
    }

    private static String _doPost(String url, String ctype, byte[] content,
                                  int connectTimeout, int readTimeout, Map<String, String> headerMap)
            throws IOException {

        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = getConnection(new URL(url), METHOD_POST, ctype,
                        headerMap);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                TaobaoLogger.logCommError(e, url, map.get("app_key"),
                        map.get("method"), content);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                TaobaoLogger.logCommError(e, conn, map.get("app_key"),
                        map.get("method"), content);
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;

    }

    private static final MediaType FORM = MediaType
            .parse("application/x-www-form-urlencoded; charset=utf-8");

    private static String _doPostA(String url, String ctype, String content,
                                   int connectTimeout, int readTimeout, Map<String, String> headerMap,
                                   Callback callback) throws IOException {
        /**
         * HttpURLConnection conn = null; OutputStream out = null; String rsp =
         * null; try { try { conn = getConnection(new URL(url), METHOD_POST,
         * ctype, headerMap); conn.setConnectTimeout(connectTimeout);
         * conn.setReadTimeout(readTimeout); } catch (IOException e) {
         * Map<String, String> map = getParamsFromUrl(url);
         * TaobaoLogger.logCommError(e, url, map.get("app_key"),
         * map.get("method"), content); throw e; } try { out =
         * conn.getOutputStream(); out.write(content); rsp =
         * getResponseAsString(conn); } catch (IOException e) { Map<String,
         * String> map = getParamsFromUrl(url); TaobaoLogger.logCommError(e,
         * conn, map.get("app_key"), map.get("method"), content); throw e; } }
         * finally { if (out != null) { out.close(); } if (conn != null) {
         * conn.disconnect(); } }
         *
         * return rsp;
         */


        /**
         List<ConnectionSpec> specs = new ArrayList<ConnectionSpec>();
         specs.add(new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build());
         specs.add(new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
         .tlsVersions(TlsVersion.TLS_1_2)
         .cipherSuites(
         CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
         CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
         CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
         .build());
         OkHttpClient httpClient = new OkHttpClient();
         httpClient.setConnectionSpecs(specs);
         */

        // String reqBody = encoder.encode(appKey, appSecret, request);
        // Request get = new Request.Builder().url(url).get().build();

        Request post = new Request.Builder().url(url)
                .post(RequestBody.create(FORM, content)).build();
        // Request post = new
        // Request.Builder().url(url).post(RequestBody.create(FORM,
        // content)).build();

        //LogUtils.d("top http request: url = " + url);

        Call call = httpClient.newCall(post);
        // Response httpResponse = call.execute();
        call.enqueue(callback);
        /*
		 * if (!httpResponse.isSuccessful()) { throw new
		 * RuntimeException("top http response error: status = " +
		 * httpResponse.code() + ", body = " + httpResponse.body().toString());
		 * }
		 * 
		 * String httpRspBody = httpResponse.body().string();
		 * LogUtils.d("top http request: httpRspBody = " + httpRspBody);
		 */
        return "";
    }

    /**
     * 执行带文件上传的HTTP POST请求。
     *
     * @param url        请求地址
     * @param textParams 文本请求参数
     * @param fileParams 文件请求参数
     * @return 响应字符串
     */
    public static String doPost(String url, Map<String, String> params,
                                Map<String, FileItem> fileParams, int connectTimeout,
                                int readTimeout) throws IOException {
        if (fileParams == null || fileParams.isEmpty()) {
            return doPost(url, params, DEFAULT_CHARSET, connectTimeout,
                    readTimeout);
        } else {
            return doPost(url, params, fileParams, DEFAULT_CHARSET,
                    connectTimeout, readTimeout);
        }
    }

    public static String doPost(String url, Map<String, String> params,
                                Map<String, FileItem> fileParams, String charset,
                                int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, params, fileParams, charset, connectTimeout,
                readTimeout, null);
    }

    /**
     * 执行带文件上传的HTTP POST请求。
     *
     * @param url        请求地址
     * @param textParams 文本请求参数
     * @param fileParams 文件请求参数
     * @param charset    字符集，如UTF-8, GBK, GB2312
     * @param headerMap  需要传递的header头，可以为空
     * @return 响应字符串
     */
    public static String doPost(String url, Map<String, String> params,
                                Map<String, FileItem> fileParams, String charset,
                                int connectTimeout, int readTimeout, Map<String, String> headerMap)
            throws IOException {
        if (fileParams == null || fileParams.isEmpty()) {
            return doPost(url, params, charset, connectTimeout, readTimeout,
                    headerMap);
        } else {
            return _doPostWithFile(url, params, fileParams, charset,
                    connectTimeout, readTimeout, headerMap);
        }
    }

    private static String _doPostWithFile(String url,
                                          Map<String, String> params, Map<String, FileItem> fileParams,
                                          String charset, int connectTimeout, int readTimeout,
                                          Map<String, String> headerMap) throws IOException {
        String boundary = String.valueOf(System.nanoTime()); // 随机分隔线
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                String ctype = "multipart/form-data;charset=" + charset
                        + ";boundary=" + boundary;
                conn = getConnection(new URL(url), METHOD_POST, ctype,
                        headerMap);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                TaobaoLogger.logCommError(e, url, map.get("app_key"),
                        map.get("method"), params);
                throw e;
            }

            try {
                out = conn.getOutputStream();
                byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n")
                        .getBytes(charset);

                // 组装文本请求参数
                Set<Entry<String, String>> textEntrySet = params.entrySet();
                for (Entry<String, String> textEntry : textEntrySet) {
                    byte[] textBytes = getTextEntry(textEntry.getKey(),
                            textEntry.getValue(), charset);
                    out.write(entryBoundaryBytes);
                    out.write(textBytes);
                }

                // 组装文件请求参数
                Set<Entry<String, FileItem>> fileEntrySet = fileParams
                        .entrySet();
                for (Entry<String, FileItem> fileEntry : fileEntrySet) {
                    FileItem fileItem = fileEntry.getValue();
                    if (fileItem.getContent() == null) {
                        continue;
                    }
                    byte[] fileBytes = getFileEntry(fileEntry.getKey(),
                            fileItem.getFileName(), fileItem.getMimeType(),
                            charset);
                    out.write(entryBoundaryBytes);
                    out.write(fileBytes);
                    out.write(fileItem.getContent());
                }

                // 添加请求结束标志
                byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n")
                        .getBytes(charset);
                out.write(endBoundaryBytes);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                TaobaoLogger.logCommError(e, conn, map.get("app_key"),
                        map.get("method"), params);
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static byte[] getTextEntry(String fieldName, String fieldValue,
                                       String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    private static byte[] getFileEntry(String fieldName, String fileName,
                                       String mimeType, String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\";filename=\"");
        entry.append(fileName);
        entry.append("\"\r\nContent-Type:");
        entry.append(mimeType);
        entry.append("\r\n\r\n");
        return entry.toString().getBytes(charset);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应字符串
     */
    public static String doGet(String url, Map<String, String> params)
            throws IOException {
        return doGet(url, params, DEFAULT_CHARSET);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @return 响应字符串
     */
    public static String doGet(String url, Map<String, String> params,
                               String charset) throws IOException {
        /**
         * HttpURLConnection conn = null; String rsp = null;
         *
         * try { String ctype = "application/x-www-form-urlencoded;charset=" +
         * charset; String query = buildQuery(params, charset); try { conn =
         * getConnection(buildGetUrl(url, query), METHOD_GET, ctype, null); }
         * catch (IOException e) { Map<String, String> map =
         * getParamsFromUrl(url); TaobaoLogger.logCommError(e, url,
         * map.get("app_key"), map.get("method"), params); throw e; }
         *
         * try { rsp = getResponseAsString(conn); } catch (IOException e) {
         * Map<String, String> map = getParamsFromUrl(url);
         * TaobaoLogger.logCommError(e, conn, map.get("app_key"),
         * map.get("method"), params); throw e; } } finally { if (conn != null)
         * { conn.disconnect(); } } return rsp;
         */
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        String getUrl = buildGetUrlString(url, query);

        List<ConnectionSpec> specs = new ArrayList<ConnectionSpec>();
        specs.add(new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT).build());
        specs.add(new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build());
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectionSpecs(specs);

        // String reqBody = encoder.encode(appKey, appSecret, request);
        Request get = new Request.Builder().url(url).get().build();
        // Request post = new
        // Request.Builder().url(url).post(RequestBody.create(FORM,
        // reqBody)).build();

        LogUtils.d("top http request: url = " + getUrl);

        Call call = httpClient.newCall(get);
        Response httpResponse = call.execute();

        if (!httpResponse.isSuccessful()) {
            throw new RuntimeException("top http response error: status = "
                    + httpResponse.code() + ", body = "
                    + httpResponse.body().toString());
        }

        String httpRspBody = httpResponse.body().string();
        LogUtils.d("top http request: httpRspBody = " + httpRspBody);

        return httpRspBody;

    }

    private static HttpURLConnection getConnection(URL url, String method,
                                                   String ctype, Map<String, String> headerMap) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection connHttps = (HttpsURLConnection) conn;
            if (ignoreSSLCheck) {
                try {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init(null,
                            new TrustManager[]{new TrustAllTrustManager()},
                            new SecureRandom());
                    connHttps.setSSLSocketFactory(ctx.getSocketFactory());
                    connHttps.setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname,
                                              SSLSession session) {
                            return true;
                        }
                    });
                } catch (Exception e) {
                    throw new IOException(e);
                }
            } else {
                try {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init(null,
                            new TrustManager[]{new VerisignTrustManager()},
                            new SecureRandom());
                    connHttps.setSSLSocketFactory(ctx.getSocketFactory());
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
            conn = connHttps;
        }

        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript");
        conn.setRequestProperty("User-Agent", "top-sdk-java");
        conn.setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    private static String buildGetUrlString(String strUrl, String query)
            throws IOException {

        if (StringUtils.isEmpty(query)) {
            return strUrl;
        }

        if (StringUtils.isEmpty(strUrl)) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }

        return strUrl;
    }

    private static URL buildGetUrl(String strUrl, String query)
            throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }

        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }

        return new URL(strUrl);
    }

    public static String buildQuery(Map<String, String> params, String charset)
            throws IOException {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=")
                        .append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":"
                        + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset)
            throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();

            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }

    /**
     * 使用默认的UTF-8字符集反编码请求参数值。
     *
     * @param value 参数值
     * @return 反编码后的参数值
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的UTF-8字符集编码请求参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用指定的字符集反编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 反编码后的参数值
     */
    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 使用指定的字符集编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 编码后的参数值
     */
    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf('?') != -1) {
            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
        }
        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    /**
     * 从URL中提取所有的参数。
     *
     * @param query URL地址
     * @return 参数映射
     */
    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap<String, String>();

        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }

}
