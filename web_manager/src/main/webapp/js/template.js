Vue.component('v-select', VueSelect.VueSelect);
new Vue({
    el: "#app",
    data: {
        tempList: [],
        temp: {},
        searchTemp: '',
        templateName: '',
        current: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPageIndex: 15,
        selectIds: [], //记录选择了哪些记录的id
        brandsOptions: [],
        placeholder: '可以进行多选',
        selectBrands: [],
        sel_brand_obj: [],
        specOptions: [],
        selectSpecs: [],
        sel_spec_obj: [],
        otherExtends: [],
        selectedId: []
    },
    methods: {
        pageHandler: function (current) {
            var _this = this;
            this.page = current;
            var request = {
                current: this.current,
                pageSize: this.pageSize,
                queryContent: this.searchTemp
            };
            axios.post("/temp/search.do", request)
                .then(function (response) {
                    let data = response.data;
                    //取服务端响应的结果
                    _this.tempList = data.rows;
                    _this.total = data.total;
                }).catch(function (reason) {
                console.log(reason);
            })
        },// 定义方法：获取JSON字符串中的某个key对应值的集合
        jsonToString: function (jsonStr, key) {
            // 将字符串转成JSOn:
            var jsonObj = JSON.parse(jsonStr);
            var value = "";
            for (var i = 0; i < jsonObj.length; i++) {
                if (i > 0) {
                    value += ",";
                }
                value += jsonObj[i][key];
            }
            return value;
        },
        getSelectionData: function () {
            var _this = this;
            axios.get("/brand/selectOptionList.do")
                .then(function (response) {
                    _this.brandsOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        selected_brand: function () {
            this.selectBrands = this.sel_brand_obj.map(function (obj) {
                return obj.id
            });
        },
        getSpecOptionData: function () {
            var _this = this;
            axios.get("/spec/selectOptionList.do")
                .then(function (response) {
                    _this.specOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        select_spec: function () {
            this.selectSpecs = this.sel_spec_obj.map(function (obj) {
                return obj.id
            });
            console.log(this.sel_spec_obj);
        },
        save: function () {
            var _this = this;
            var entity = {
                name: this.templateName,
                specIds: this.sel_spec_obj,
                brandIds: this.sel_brand_obj,
                customAttributeItems: this.otherExtends
            };
            axios.post("/temp/save.do", entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    alert(response.data.message)
                    window.location.reload();
                }
            }).catch(function (reason) {
                console.log(reason);
            });
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
            console.log(this.selectedId)
        },
        deleteTemplate: function () {
            if (this.selectedId.length == 0) {
                alert("至少选中一行删除!");
                return;
            }
            var _this = this;
            let param = Qs.stringify({ids: _this.selectedId}, {indices: false});
            axios.post("/temp/delete.do", param).then((res) => {
                let data = res.data;
                if (data.success) {
                    window.location.reload();
                    alert(data.message);
                } else {
                    alert(data.message);
                }
            })
        },
    },
    created: function () {
        this.pageHandler(1);
        this.getSelectionData();
        this.getSpecOptionData();
    }
});