new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 0,
        searchEntity: {},
        categoryList: [],
        selectIds: [] //记录选择了哪些记录的id
    },
    methods: {
        pageHandler: function (current) {
            let _this = this;
            this.page = current;
            axios.post("/contentCategory/search.do?current=" + current + "&pageSize=" + this.pageSize, this.searchEntity)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.categoryList = response.data.rows;
                    _this.total = response.data.total;

                }).catch(function (reason) {
                console.log(reason);
            })
        }
    },
    created: function () {
        this.pageHandler(1);
    },
});