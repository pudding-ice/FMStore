new Vue({
    el: "#app",
    data: {
        seller: '',

    },
    methods: {
        getLoginDetail: function () {
            var _this = this;
            axios.get("/index/getLoginDetail.do").then(function (response) {
                var data = response.data;
                _this.seller = data;
            }).catch(function (reason) {
                console.log(reason);
            })
        },

    },
    created: function () {
        this.getLoginDetail();
    }
});
