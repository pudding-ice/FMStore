new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 15,
        goodsList: [],
        categoryList: [],
        selectedId: [],
        queryParam: {
            status: -1,
            goodsName: ''
        },

    },
    methods: {
        pageHandler: function (current) {
            let _this = this;
            this.current = current;
            let pageRequest = {
                current: current,
                pageSize: this.pageSize,
                queryContent: this.queryParam.status + "," + this.queryParam.goodsName
            }
            axios.post("/goods/getPage.do?", pageRequest)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.goodsList = response.data.rows;
                    _this.total = response.data.total;
                    // console.log(response.data.rows);
                    // console.log(_this.categoryList[_this.goodsList[0].category1Id])
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        async getCategoryList() {
            let _this = this;
            await axios.get("/goods/getCategory.do").then((res) => {
                let data = res.data;
                for (let i = 0; i < data.length; i++) {
                    _this.categoryList[data[i].id] = data[i].name;
                }
            })
            this.pageHandler(1);

            // console.log(this.categoryList);
        },
        deleteGoods: function () {
            if (this.selectedId.length == 0) {
                alert("至少选中一行删除!");
                return;
            }
            let _this = this;
            let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
            axios.post('/goods/delete.do', param).then((res) => {
                if (res.data.success) {
                    window.location.reload();
                    _this.selectedId = [];
                    alert(res.data.message);
                } else {
                    alert(res.data.message);
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
        submitAudit: function () {
            let _this = this;
            if (this.selectedId.length === 0) {
                alert("至少选中一行提交!");
                return;
            }
            for (let i = 0; i < this.selectedId.length; i++) {
                if (_this.findHaveAuditingOrAccept(this.selectedId[i])) {
                    //包含正在审核的商品或者已经通过审核的商品
                    alert("选择的商品中包含正在审核或者已经通过审核的商品,请重新选择");
                    return;
                }
            }
            let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
            axios.post("/goods/sellerSubmitAudit.do", param).then((res) => {
                let data = res.data;
                if (data.success) {
                    alert(data.message);
                    window.location.reload();
                    _this.selectedId = [];
                } else {
                    alert(data.message);
                }
            })
        },
        findHaveAuditingOrAccept: function (id) {
            let flag = false;
            this.goodsList.forEach((goods) => {
                if (goods.id === id) {
                    if (goods.auditStatus === '1') {
                        flag = true;
                    } else if (goods.auditStatus === '2') {
                        flag = true;
                    }
                }
            })
            return flag;
        },
        initDataMethod: function () {
            this.getCategoryList();
        },
        queryGoods: function () {
            console.log("查询")
            console.log(this.queryParam)
            let _this = this;
            axios.get("/seller/queryGoods/" + _this.queryParam.status + "/" + _this.queryParam.goodsName + ".do").then((res) => {

            })
        }

    },
    created() {
        this.initDataMethod();
    }
});
