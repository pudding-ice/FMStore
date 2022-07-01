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
        selectedId: [],

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
        },
        handleSelected: function (event, id) {
            if (event.target.checked) {
                //选中
                this.selectedId.push(id);
            } else {
                //取消选中
                let idx = this.selectedId.indexOf(id);
                this.selectedId.splice(idx, 1)
            }
            console.log(this.selectedId);
        },
        chooseAll: function (event) {
            let inp = document.getElementsByTagName('input');
            if (event.target.checked) {
                //全选
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = true;
                }
                for (let i = 0; i < this.sellerList.length; i++) {
                    this.selectedId.push(this.sellerList[i].sellerId);
                }
            } else {
                //取消选中
                for (let i = 0; i < inp.length; i++) {
                    inp[i].checked = false;
                }
                for (let i = 0; i < this.sellerList.length; i++) {
                    let idx = this.selectedId.indexOf(this.sellerList[i].sellerId);
                    this.selectedId.splice(idx, 1);
                }
            }
            // console.log(this.selectedId)
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