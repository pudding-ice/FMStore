Vue.component('v-select', VueSelect.VueSelect);
new Vue({
    el: "#app",
    data: {
        current: 1,
        pageSize: 10,
        total: 0,
        maxPageIndex: 15,
        categoryList: [],
        allCategoryList: [],
        breadList: [{
            id: 0,
            name: "顶级分类列表"
        }],//面包屑导航列表
        placeholder: '选择一个模板',
        selectTemp: [],
        currentItemCat: {},
        tempOptions: [],
        sel_temp_obj: [],
    },
    methods: {
        pageHandler: function (current) {
            let _this = this;
            _this.current = current;
            let args = {
                current: current,
                pageSize: this.pageSize,
                queryContent: this.allCategoryList[0].parentId
            }
            axios.post("/itemCate/getPage.do", args)
                .then(function (response) {
                    //取服务端响应的结果
                    let data = response.data;
                    if (data.rows === null) {
                        alert("没有下一级了")
                        let parentId = _this.categoryList[0].parentId;
                        _this.selectCateByParentId(parentId)
                    } else {
                        _this.categoryList = data.rows;
                        _this.total = data.total;
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        selectAllTemplate: function (id) {
            let _this = this
            axios.get("/itemCate/getAllTemplate.do").then((res) => {
                let data = res.data;
                _this.tempOptions = data;
            })
        },
        select_temp: function () {
            this.selectTemp = this.sel_temp_obj.map(function (obj) {
                return obj.id
            });
            console.log(this.selectTemp);
        },
        selectCateByParentId: function (id, name) {
            let item = {
                id: id,
                name: name,
            }
            this.breadHandler(item);
            var _this = this;
            axios.post("/itemCate/findByParentId/" + id + ".do")
                .then(function (response) {
                    //取服务端响应的结果
                    let data = response.data;
                    if (data === null) {
                        alert("没有下一级了")
                        let parentId = _this.categoryList[0].parentId;
                        _this.selectCateByParentId(parentId)
                    } else {
                        _this.allCategoryList = data;
                        _this.pageHandler(1);
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        breadHandler: function (item) {
            let breadList = this.breadList;
            if (breadList[breadList.length - 1].id == item.id) {
                return;
            } else if (breadList.indexOf(item) > -1) {
                //如果存在,而且不是最后一个,就是删除
                let index = breadList.indexOf(item);
                //计算要删除的数量
                let delCount = breadList.length - index - 1;
                breadList.splice(index + 1, delCount);
            } else {
                //如果不存在,就是添加
                breadList.push(item);
            }
            this.selectCateByParentId(item.id);
        },
        loadItemCatData: function (id) {
            this.flushData();
            let findOneById = this.findOneById(this.categoryList, id);
            if (findOneById !== null) {
                let parse = JSON.parse(JSON.stringify(findOneById));
                this.currentItemCat = parse;
            }
        },
        flushData: function () {
            this.currentItemCat.name = '';
        },
        findOneById: function (list, id) {
            let _this = this;
            for (let i = 0; i < list.length; i++) {
                if (list[i].id === id) {
                    return list[i];
                }
            }
            return null;
        },
        getTempName: function (id) {
            for (let i = 0; i < this.tempOptions.length; i++) {
                if (this.tempOptions[i].id === id) {
                    return this.tempOptions[i].name
                }
            }
        }
    },
    created: function () {
        this.selectCateByParentId(0, '');
        this.selectAllTemplate()
    }
});


