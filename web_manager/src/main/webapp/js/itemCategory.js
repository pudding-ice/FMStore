new Vue({
    el: "#app",
    data: {
        categoryList: [],
    },
    methods: {
        selectCateByParentId: function (id) {
            _this = this;
            axios.get("/itemCate/findByParentId/" + id + ".do")
                .then(function (response) {
                    //取服务端响应的结果
                    _this.categoryList = response.data;
                    console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
    },
    created: function () {
        this.selectCateByParentId(0);
    }
});
