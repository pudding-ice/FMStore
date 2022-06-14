new Vue({
    el: "#app",
    data: {
        current: 1,
        pageSize: 10,
        total: 0,
        maxPageIndex: 15,
        sellerList: [],//保存所有待审核商家列表
        searchSeller: {},//搜索审核关键字
        sellerDetail: {},//回显的商家详细数据
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
        detail: function (id) {
            var _this = this;
            _this.sellerList.forEach((item) => {
                if (id === item.sellerId) {
                    _this.sellerDetail = item;
                }
            })
        },
        updateStatus: function (id, status) {
            var _this = this;
            axios.get("/seller/updateStatus/" + id + "/" + status + ".do").then(function (response) {
                let data = response.data;
                if (data.success) {
                    //取服务端响应的结果
                    alert(data.message);
                    _this.pageHandler(1);
                } else {
                    alert(data.message);
                }
            })
        }
    },
    created: function () {
        this.pageHandler(1)
    }
});