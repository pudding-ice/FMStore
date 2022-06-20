new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 15,
        searchContent: '',
        goodsList: [],
        categoryList: {},
        selectedId: [],

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
        },
        async getCategoryList() {
            let _this = this;
            await axios.get("/goods/getCategory.do").then((res) => {
                let data = res.data;
                data.forEach((item) => {
                    _this.categoryList[item.id] = item.name;
                })
            })
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
    },
    async created() {
        await this.getCategoryList();
        this.pageHandler(1);
    }
});
