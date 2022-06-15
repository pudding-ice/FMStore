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
            var item = {
                id: id,
                name: name,
            }
            this.breadHandler(item);
            var _this = this;
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
            console.log(this.breadList);
        }
    },
    created: function () {
        this.selectCateByParentId(0);
    }
});


