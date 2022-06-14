new Vue({
    el: "#app",
    data: {
        categoryList: [],
        breadList: [{
            id: 0,
            name: "顶级分类列表"
        }],//面包屑导航列表
    },
    methods: {
        selectCateByParentId: function (id, name) {
            var _this = this;
            if (id != 0) {
                this.breadList.push({id, name});
            }
            axios.get("/itemCate/findByParentId/" + id + ".do")
                .then(function (response) {
                    //取服务端响应的结果
                    let data = response.data;
                    if (data === null) {
                        alert("没有下一级了")
                        let parentId = _this.categoryList[0].parentId;
                        _this.selectCateByParentId(parentId)
                    } else {
                        _this.categoryList = data;
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        breadHandler: function (id) {
            if (id === 0) {
                this.selectCateByParentId(0);
                return;
            }
            var _this = this;
            var flag = false;
            this.breadList.forEach((item) => {
                if (flag) {
                    let index = _this.breadList.indexOf(item);
                    _this.breadList.splice(index, 1);
                }
                if (item.id === id) {
                    flag = true;
                }
            })
            console.log(_this.breadList);
            this.selectCateByParentId(id);
        }
    },
    created: function () {
        this.selectCateByParentId(0);
    }
});


