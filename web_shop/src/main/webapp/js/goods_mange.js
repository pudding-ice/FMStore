new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 15,
        searchContent: '',
        goodsList: []
    },
    methods: {
        pageHandler: function (current) {
            let _this = this;
            this.current = current;
            let pageRequest = {
                current: current,
                pageSize: this.pageSize,
                queryContent: this.searchContent
            }
            axios.post("/goods/getPage.do?", pageRequest)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.goodsList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response.data.rows);
                }).catch(function (reason) {
                console.log(reason);
            })
        }
    },
    created: function () {
        this.pageHandler(1);
    }
});
