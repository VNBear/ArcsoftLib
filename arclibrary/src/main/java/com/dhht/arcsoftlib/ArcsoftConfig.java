package com.dhht.arcsoftlib;

// ┏┓　　　┏┓
// ┏┛┻━━━┛┻┓
// ┃　　　　　　　┃ 　
// ┃　　　━　　　┃
// ┃　┳┛　┗┳　┃
// ┃　　　　　　　┃
// ┃　　　┻　　　┃
// ┃　　　　　　　┃
// ┗━┓　　　┏━┛
// ┃　　　┃ 神兽保佑　　　　　　　　
// ┃　　　┃ 代码无BUG！
// ┃　　　┗━━━┓
// ┃　　　　　　　┣┓
// ┃　　　　　　　┏┛
// ┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛

/**
 * CreateDate：2018/10/16
 * Creator： VNBear
 * Description:
 **/
public class ArcsoftConfig {

    private String appid;
    private String sdk_key;
    private String fd_key;
    private String ft_key;
    private String fr_key;
    private String age_key;
    private String gender_key;

    public String getAppid() {
        return appid;
    }

    public String getSdk_key() {
        return sdk_key;
    }

    public String getFd_key() {
        return fd_key;
    }

    public String getFt_key() {
        return ft_key;
    }

    public String getFr_key() {
        return fr_key;
    }

    public String getAge_key() {
        return age_key;
    }

    public String getGender_key() {
        return gender_key;
    }

    private ArcsoftConfig() {

    }

    public static final class Builder {
        private String appid = "2TDK7UrL9BD9K1qYPnizhyJqcL4yfsHso9itXUTvfyhK";
        private String sdk_key = "Fvb9JwLez1xjmx8LzhPjtxrHFWuSxbRXyz1WcF6HL34P";
        private String fd_key = "Fvb9JwLez1xjmx8LzhPjtxrA67eD2GAk4Xxr2GHF1cKJ";
        private String ft_key = "Fvb9JwLez1xjmx8LzhPjtxr2viP69QPkst54QEYd9Qrg";
        private String fr_key = "Fvb9JwLez1xjmx8LzhPjtxrejigwBCUTRuoTcXrxNuLj";
        private String age_key = "Fvb9JwLez1xjmx8LzhPjtxru4XDG518ReERmnpduTz1F";
        private String gender_key = "Fvb9JwLez1xjmx8LzhPjtxs2DvUV3pPF4Zd42xokFyQJ";

        public Builder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder setSdk_key(String sdk_key) {
            this.sdk_key = sdk_key;
            return this;
        }

        public Builder setFd_key(String fd_key) {
            this.fd_key = fd_key;
            return this;
        }

        public Builder setFt_key(String ft_key) {
            this.ft_key = ft_key;
            return this;
        }

        public Builder setFr_key(String fr_key) {
            this.fr_key = fr_key;
            return this;
        }

        public Builder setAge_key(String age_key) {
            this.age_key = age_key;
            return this;
        }

        public Builder setGender_key(String gender_key) {
            this.gender_key = gender_key;
            return this;
        }

        public ArcsoftConfig builder() {
            ArcsoftConfig config = new ArcsoftConfig();
            config.appid = this.appid;
            config.sdk_key = this.sdk_key;
            config.fd_key = this.fd_key;
            config.ft_key = this.ft_key;
            config.fr_key = this.fr_key;
            config.age_key = this.age_key;
            config.gender_key = this.gender_key;
            return config;
        }
    }
}
