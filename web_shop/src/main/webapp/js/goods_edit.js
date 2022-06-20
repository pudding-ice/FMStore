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
        currentTemplate: '',
        specSelList: [],
        rowList: [],
        goodsEntity: {
            goods: {
                goodsName: '',
                caption: ''
            },
            goodsDesc: {
                specificationItems: []
            },
            itemList: {}
        },
        isEnableSpec: 1
    },

    methods: {
        loadCateData(id) {
            let _this = this;
            axios.get("/itemCate/findByParentId/" + id + ".do")
                .then(function (response) {
                    console.log("grade =" + _this.grade);
                    console.log(response.data)
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
            if (this.currentImage.color === '') {
                alert("必须填写图片颜色");
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

        },
        deleteImg: function (index) {
            var _this = this;
            //获取要删除的图片URL
            var url = this.imgList[index].url;
            axios.get("/fdfs/deleteImg.do?url=" + url)
                .then(function (response) {
                    let data = response.data;
                    if (data.success) {
                        // 从数组中移除
                        _this.imgList.splice(index, 1);
                        alert(data.message);
                    } else {
                        alert(data.message);
                    }
                })
        },
        //专门用来寻找列表中的对象
        findObjectByKey: function (list, key, value) {
            for (let i = 0; i < list.length; i++) {
                if (list[i][key] === value) {
                    return list[i];
                }
            }
            return null;
        },
        updateSpecListStatus: function (event, specName, option) {
            let findObj = this.findObjectByKey(this.specSelList, "specName", specName);
            if (findObj != null) {
                if (event.target.checked) {
                    //处理选中
                    this.specSelList.forEach((item) => {
                        if (item.specName === specName) {
                            item.specOptions.push(option)
                        }
                    });
                } else {
                    //处理取消选中
                    let idx = findObj.specOptions.indexOf(option);
                    findObj.specOptions.splice(idx, 1);
                    if (findObj.specOptions.length === 0) {
                        let idx = this.specSelList.indexOf(findObj);
                        this.specSelList.splice(idx, 1);
                    }
                }
            } else {
                //不存在,就添加
                this.specSelList.push({
                    "specName": specName,
                    "specOptions": [option]
                })
            }
            console.log(this.specSelList)
        },
        saveGoods: function () {
            let entity = this.goodsEntity;
            //商品基本信息
            entity.goods.brandId = this.selectBrand;
            entity.goods.category1Id = this.cateSelected1;
            entity.goods.category2Id = this.cateSelected2;
            entity.goods.category3Id = this.cateSelected3;
            entity.goods.typeTemplateId = this.typeId;
            entity.goods.isEnableSpec = this.isEnableSpec;
            //商品描述信息
            entity.goodsDesc.introduction = UE.getEditor('editor').getContent();
            entity.goodsDesc.specificationItems = this.specSelList;
            entity.goodsDesc.itemImages = this.imgList;
            //商品规格信息
            entity.itemList = this.rowList;
            //判断前端取值再发送给后端
            if (entity.goods.cateSelected3 === null || -1 === entity.goods.cateSelected3) {
                alert("请选择商品分类");
                return;
            }
            if (entity.goods.goodsName === null || '' === entity.goods.goodsName) {
                alert("请填写商品名称");
                return;
            }
            if (entity.goods.brandId === null || -1 === entity.goods.brandId) {
                alert("请选择品牌");
                return;
            }
            if (entity.goods.caption === null || '' === entity.goods.caption) {
                alert("请填写商品副标题");
                return;
            }
            if (entity.goods.price === null || '' === entity.goods.price) {
                alert("请填写商品价格");
                return;
            }

            if (entity.goodsDesc.itemImages === null || entity.goodsDesc.itemImages.length === 0) {
                alert("请上传商品图片");
                return;
            }
            if (entity.goods.isEnableSpec === 1) {
                if (entity.goodsDesc.specificationItems === null || entity.goodsDesc.specificationItems.length === 0) {
                    alert("已启用商品规格,请选择商品规格")
                    return;
                }
            }
            //通过全部验证,发送数据给后端
            console.log(this.goodsEntity);
            axios.post("/goods/save.do", this.goodsEntity).then((res) => {

            })
        },
        getQueryString: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return (r[2]);
            return null;
        },
        checkSpecStatus: function (specName, optionName) {
            console.log(this.specSelList);
            for (let i = 0; i < this.specSelList.length; i++) {
                if (this.specSelList[i].specName === specName) {
                    for (let j = 0; j < this.specSelList[i].specOptions.length; j++) {
                        if (this.specSelList[i].specOptions[j].optionName === optionName) {
                            return true;
                        }
                    }
                }
            }
            return false;
        },
    },
    watch: {
        typeId: function (newValue, oldValue) {
            var _this = this;
            if (newValue == 0) {
                return;
            }
            axios.get("/temp/getOne/" + newValue + ".do").then((res) => {
                // console.log(res.data);
                let data = JSON.parse(res.data.brandIds);
                _this.brandList = data;
                if (_this.goodsEntity.goods.brandId != null) {
                    _this.selectBrand = _this.goodsEntity.goods.brandId;
                }
            })
            // 查询模板中对应的规格信息
            axios.get("/temp/getSpecificationById/" + newValue + ".do").then((res) => {
                _this.currentTemplate = res.data;
            })
        },
        specSelList: {
            handler: function () {
                console.log("构造规格列表")
                let specList = JSON.parse(JSON.stringify(this.specSelList));
                let rowList = [
                    {spec: {}, price: 0, num: 0, status: '0', isDefault: '0'}
                ]
                for (let i = 0; i < specList.length; i++) {
                    //总遍历次数
                    let oneSpec = specList[i];
                    let specName = oneSpec.specName; // "选择版本"
                    let options = oneSpec.specOptions;  //specOptions: Array [ {…}, {…} ]
                    let newRowList = []
                    for (let j = 0; j < rowList.length; j++) {
                        //这里循环边界 rowList.length 是动态的
                        for (let k = 0; k < options.length; k++) {
                            let oldRow = JSON.parse(JSON.stringify(rowList[j]));
                            oldRow.spec[specName] = options[k].optionName;
                            newRowList.push(oldRow);
                        }
                    }
                    rowList = newRowList;
                }
                this.rowList = rowList;
                console.log(rowList)
            },
            deep: true
        }


    },
    created: function () {
        this.loadCateData(0);
    },
    mounted() {
        let id = this.getQueryString("id");
        let _this = this;
        if (id != null) {
            //根据id查询当前商品
            // console.log(id);
            axios.get("/goods/getOneById/" + id + ".do").then((res) => {
                let data = res.data;
                console.log(data);
                _this.goodsEntity.goods = data.goods;
                _this.goodsEntity.goodsDesc = data.goodsDesc;
                _this.goodsEntity.itemList = data.itemList;
                //回显富文本
                UE.getEditor("editor").ready(function () {
                    UE.getEditor("editor").setContent(data.goodsDesc.introduction);
                });
                //回显富文本
                _this.typeId = data.goods.typeTemplateId;
                //回显商品图片列表
                _this.imgList = JSON.parse(data.goodsDesc.itemImages);
                //回显选中规格
                _this.specSelList = JSON.parse(data.goodsDesc.specificationItems);
                //回显库存列表
                _this.rowList = data.itemList;
                _this.rowList.forEach((item) => {
                    JSON.parse(item.spec)
                });
                _this.cateSelected1 = data.goods.category1Id;
                if (data.goods.category2Id > -1) {
                    _this.grade = 2;
                    _this.cateSelected2 = data.goods.category2Id;
                    _this.loadCateData(_this.cateSelected1);
                    // console.log(_this.cateSelected2);
                }
                if (data.goods.category3Id > -1) {
                    _this.grade = 3;
                    _this.cateSelected3 = data.goods.category3Id;
                    _this.loadCateData(_this.cateSelected2);
                    // console.log(_this.cateSelected3);
                }
            })
        }

    },

});


