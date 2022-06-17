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
        brandList: null,
        currentImage: {
            color: '',
            url: ''
        },
        imgList: [],

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
        },
        uploadImg: function () {
            //先检查
            if (this.currentImage.url == '' || this.currentImage.color == '') {
                alert("必须上传图片并且填写图片颜色");
                return;
            }
            let formData = new FormData();
            formData.append('file', file.files[0]);
            let instance = axios.create({
                withCredentials: true
            });
            var _this = this;
            instance.post('/fdfs/upload.do', formData).then((res) => {
                let data = res.data;
                if (data.success) {
                    //如果上传成功,使用实例对象的message字段存储URL信息(fastdfs上传成功后返回的路径)
                    _this.currentImage.url = data.message
                } else {
                    console.log(data.message);
                }
            })
        },
        reloadForm: function () {
            this.currentImage.color = '',
                this.currentImage.url = ''
        },
        saveImage: function () {
            if (this.currentImage.color == '') {
                alert("图片颜色不能为空");
            }
            if (this.currentImage.url == '') {
                alert("请上传图片");
            }
            var obj = {
                color: this.currentImage.color,
                url: this.currentImage.url
            }
            this.imgList.push(obj);

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
