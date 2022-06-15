new Vue({
    el: "#app",
    data: {
        categoryList1: [],//分类1数据列表
        categoryList2: [],//分类2数据列表
        categoryList3: [],//分类3数据列表
        grade: 1,  //记录当前级别
        cateSelected1: -1,//分类1选中的id
        cateSelected2: -1,//分类2选中的id
        cateSelected3: -1,//分类3选中的id,
        typeId: 0,
        selectBrand: -1,
        brandList: null
    },
    methods: {
        loadCateData: function (id) {
            var _this = this;
            axios.get("/itemCate/findByParentId/" + id + ".do")
                .then(function (response) {
                    if (_this.grade == 1) {
                        //取服务端响应的结果
                        _this.categoryList1 = response.data;
                    }
                    if (_this.grade == 2) {
                        //取服务端响应的结果
                        _this.categoryList2 = response.data;
                    }
                    if (_this.grade == 3) {
                        //取服务端响应的结果
                        _this.categoryList3 = response.data;
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        getCateSelected: function (grade) {//选项改变时调用
            //重置模板
            this.typeId = 0;
            if (grade == 1) { //第1级选项改变
                this.categoryList2 = [];//清空二级分类数据
                this.catSelected2 = -1;   //默认选择
                this.categoryList3 = []; //清空三级分类数据
                this.catSelected3 = -1; //默认选择
                this.grade = grade + 1; // 加载第2级的数据
                this.loadCateData(this.cateSelected1);
            }
            if (grade == 2) { //第2级选项改变
                this.categoryList3 = [];//清空三级分类数据
                this.catSelected3 = -1;//默认选择
                this.grade = grade + 1;// 加载第3级的数据
                this.loadCateData(this.cateSelected2);
            }
            if (grade == 3) { //第3级选项改变
                //找到对应的模板
                let select = this.cateSelected3;
                this.categoryList3.forEach((item) => {
                    if (item.id == select) {
                        this.typeId = item.typeId;
                    }
                })
            }
        }

    },
    watch: {
        typeId: function (newValue, oldValue) {
            var _this = this;
            if (newValue == 0) {
                return;
            }
            axios.get("/temp/getOne/" + newValue + ".do").then((res) => {
                let data = JSON.parse(res.data.brandIds);
                _this.brandList = data;
            }).catch(reason => {
                console.log(reason)
            })
        }
    },
    created: function () {
        this.loadCateData(0);
    }
});
