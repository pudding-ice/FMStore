new Vue({
    el: "#app",
    data: {
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 0,
        searchEntity: {},
        categoryList: [],
        selectIds: [], //记录选择了哪些记录的id
        categoryEntity: {
            name: ''
        }
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
        },
        saveCategory: function () {
            let _this = this;
            var url = "";
            if (_this.categoryEntity.id != null) {
                url = "/contentCategory/update.do";
            } else {
                url = "/contentCategory/add.do";
            }
            axios.post(url, _this.categoryEntity)
                .then((res) => {
                    if (res.data.success) {
                        alert(res.data.message)
                        //刷新页面
                        _this.categoryEntity = {};
                        window.location.reload();
                    } else {
                        alert(res.data.message)
                    }

                });
        },
        findOne: function (id) {
            for (let i = 0; i < this.categoryList.length; i++) {
                if (this.categoryList[i].id === id) {
                    this.categoryEntity = JSON.parse(JSON.stringify(this.categoryList[i]));
                }
            }
        },
        updateSelection: function (event, id) {
            // 复选框选中
            if (event.target.checked) {
                // 向数组中添加元素
                this.selectIds.push(id);
            } else {
                // 从数组中移除
                var idx = this.selectIds.indexOf(id);
                this.selectIds.splice(idx, 1);
            }
        },
        deleteCategory: function () {
            var _this = this;
            //使用qs插件 处理数组
            axios.post('/contentCategory/delete.do', Qs.stringify({ids: _this.selectIds}, {indices: false}))
                .then((response) => {
                    if (response.data.success) {
                        _this.selectIds = [];
                        alert(response.data.message)
                        window.location.reload();
                    }
                }).catch(function (reason) {
                alert(reason.message);
            })
        }
    },
    created: function () {
        this.pageHandler(1);
    },
});