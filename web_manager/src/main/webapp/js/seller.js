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
        handleSelectedStatus: function (event) {
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
        chooseAllSeller: function (event) {
            let inp = document.getElementsByName('sellerCheck');
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
        closeSeller: function () {
            if (this.selectedId.length === 0) {
                alert("至少选中一个商家关闭!");
                return;
            }
            if (this.hasClosedSeller(this.selectedId)) {
                alert("不能选择关闭的商家!");
                return;
            }
            var ids = Qs.stringify({ids: this.selectedId}, {indices: false})
            var _this = this;
            axios.post("/seller/closeSeller.do", ids).then(function (response) {
                let res = response.data;
                if (res.success) {
                    _this.selectedId = [];
                    $("input[name='sellerCheck']").prop("checked", false);
                    window.location.reload();
                    alert(res.message)
                } else {
                    alert(res.message)
                }
            }).catch(function (reason) {
                console.log(reason);
            })
        },
        openSeller: function () {
            let _this = this;
            if (this.selectedId.length === 0) {
                alert("至少选中一行开启商家!");
                return;
            }
            if (this.hasNotClosedStatus(this.selectedId)) {
                alert("只能选择关闭状态的商家开启!");
                return;
            }
            var ids = Qs.stringify({ids: this.selectedId}, {indices: false})
            axios.post("/seller/openSeller.do", ids).then(function (response) {
                let res = response.data;
                if (res.success) {
                    _this.selectedId = [];
                    $("input[name='sellerCheck']").prop("checked", false);
                    window.location.reload();
                    alert(res.message)
                } else {
                    alert(res.message)
                }

            }).catch(function (reason) {
                console.log(reason);
            })
        },
        hasNotClosedStatus: function (ids) {
            for (let i = 0; i < ids.length; i++) {
                let id = ids[i];
                let oneSeller = this.findOneSellerById(id);
                if (oneSeller.status !== "3") {
                    return true
                }
            }
            return false;
        },
        hasClosedSeller: function (ids) {
            for (let i = 0; i < ids.length; i++) {
                let id = ids[i];
                let oneSeller = this.findOneSellerById(id);
                if (oneSeller.status === "3") {
                    return true
                }
            }
            return false;
        },
        findOneSellerById(id) {
            for (let i = 0; i < this.sellerList.length; i++) {
                if (this.sellerList[i].sellerId === id) {
                    return this.sellerList[i];
                }
            }
            return null;
        }

    },
    created: function () {
        this.pageHandler(1)
    }
});