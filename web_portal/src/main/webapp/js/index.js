new Vue({
    el: "#app",
    data: {
        contentList: [],
        searchContent: '',
        user: ''
    },
    methods: {
        loadCategoryDataById: function (categoryId) {
            let _this = this;
            axios.post("/content/findByCategoryIdFromRedis/" + categoryId + ".do")
                .then(function (response) {
                    console.log(response.data);
                    //取服务端响应的结果
                    _this.contentList[categoryId] = response.data;
                    // 在全局对象上通知
                    Vue.set(_this.contentList, categoryId, _this.contentList[categoryId]);
                }).catch(function (reason) {
                console.log(reason.data);
            });
        },
        getUserInfo: function () {
            let _this = this;
            axios.get("/user/getUserInfo.do").then((res) => {
                let data = res.data;
                _this.user = data;
            })
        },
    },
    created: function () {
        this.loadCategoryDataById(1);
        this.getUserInfo();
    },
});
