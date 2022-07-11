new Vue({
    el: "#app",
    data: {
        user: ''
    },
    methods: {
        getUserInfo: function () {
            let _this = this;
            axios.get("/user/getUserInfo.do").then((res) => {
                let data = res.data;
                _this.user = data;
            })
        }
    },
    watch: {},
    created: function () {
        this.getUserInfo();
    }
});
