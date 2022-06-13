new Vue({
    el: "#app",
    data: {
        loginName: ''
    },
    methods: {
        getLoginName: function () {
            _this = this;
            axios.post("/login/getLoginName.do")
                .then(function (response) {
                    _this.loginName = response.data.username;
                    console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        }
    },
    created: function () {
        this.getLoginName();
    }
});