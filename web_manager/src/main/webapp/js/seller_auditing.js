new Vue({
    el: "#app",
    data: {
        current: 1,
        pageSize: 10,
        total: 0,
        maxPageIndex: 15,
        sellerList: [],//保存所有待审核商家列表
        searchSeller: {},//搜索审核关键字
    },
    methods: {
        pageHandler: function (current) {
            //设置当前页码为点击的页码
            this.current = current
            var _this = this;
            var args = {
                current: current,
                pageSize: _this.pageSize,
                queryContent: _this.searchSeller
            }
            axios.post("/seller/getPage.do", args).then((response) => {
                var data = response.data;
                _this.sellerList = data.rows;
                _this.total = data.total;
            }).catch(function (reason) {
                console.log(reason)
            })
        },
    },
    created: function () {
        this.pageHandler(1)
    }
});