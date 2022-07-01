new Vue({
    el: "#app",
    data: {
        current: 1,
        pageSize: 10,
        total: 0,
        maxPageIndex: 15,
        sellerList: [],
        searchSeller: {
            sellerStatus: []
        },//搜索审核关键字
        sellerDetail: {},//回显的商家详细数据
        selectedId: [],

    },
    methods: {
        pageHandler: function (current) {
            //设置当前页码为点击的页码
            this.current = current
            let _this = this;
            let stringifyStatus = JSON.stringify(_this.searchSeller.sellerStatus)
            let args = {
                current: current,
                pageSize: _this.pageSize,
                // queryContent: _this.searchSeller.sellerStatus
                queryContent: {
                    name: _this.searchSeller.name,
                    nickName: _this.searchSeller.nickName,
                    status: stringifyStatus
                }
            };
            axios.post("/seller/getAllPage.do", args).then((response) => {
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
        },
        handleSelected: function (event) {
            if (event.target.checked) {
                //选中
                this.searchSeller.sellerStatus.push(event.target.value);
            } else {
                //取消选中
                let idx = this.searchSeller.sellerStatus.indexOf(event.target.value);
                this.searchSeller.sellerStatus.splice(idx, 1)
            }
            // console.log(this.searchSeller.sellerStatus)
        },
        chooseAll: function (event) {
            let inp = document.getElementsByName('sellerStatus');
            if (event.target.checked) {
                //全选
                for (let i = 0; i < inp.length; i++) {
                    let idx = this.searchSeller.sellerStatus.indexOf(inp[i].value);
                    inp[i].checked = true;
                    console.log(idx)
                    if (idx === -1) {
                        this.searchSeller.sellerStatus.push(inp[i].value);
                    }
                }
            } else {
                //取消选中
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = false;
                    let idx = this.searchSeller.sellerStatus.indexOf(inp[i].value);
                    if (idx !== -1) {
                        this.searchSeller.sellerStatus.splice(idx, 1);
                    }
                }
            }
            // console.log(this.searchSeller.sellerStatus)
        },
        auditAccept: function () {
            let _this = this;
            if (this.selectedId.length === 0) {
                alert("至少选中一行审核通过!");
                return;
            }
            var ids = Qs.stringify({ids: this.selectedId}, {indices: false})
            axios.post("/seller/auditAccept.do", ids).then(function (response) {
                let res = response.data;
                if (res.success) {
                    _this.selectedId = [];
                    $("input[type='checkbox']").prop("checked", false);
                    window.location.reload();
                    alert(res.message)
                } else {
                    alert(res.message)
                }

            }).catch(function (reason) {
                console.log(reason);
            })
        },
        rejectApply: function () {
            if (this.selectedId.length === 0) {
                alert("至少选中一行驳回!");
                return;
            }
            var ids = Qs.stringify({ids: this.selectedId}, {indices: false})
            var _this = this;
            axios.post("/seller/rejectApply.do", ids).then(function (response) {
                let res = response.data;
                if (res.success) {
                    _this.selectedId = [];
                    $("input[type='checkbox']").prop("checked", false);
                    window.location.reload();
                    alert(res.message)
                } else {
                    alert(res.message)
                }
            }).catch(function (reason) {
                console.log(reason);
            })
        },

    },
    created: function () {
        this.pageHandler(1)
    }
});