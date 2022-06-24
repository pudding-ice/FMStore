new Vue({
    el: "#app",
    data: {
        goPage: 1,
        total: 100,
        maxPageIndex: 15,
        resultMap: {
            rows: [],
            categoryList: [],
            bandList: [],
            specificationList: [],
            total: 0,
            totalPages: 0,
        },
        searchMap: {
            'keywords': '',//搜索关键字
            'category': '',//分类
            'brand': '',//品牌
            'spec': {},//规格
            'price': '',//价格
            'pageNo': 1,//当前页
            'pageSize': 5,//每页展示多少条数据
            'sort': '',//排序
            'sortField': '',//排序的字段
        },
    },
    methods: {
        getQueryString: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return (decodeURI(r[2]));
            }
            return null;
        },
        search: function () {
            console.log(this.searchMap);
            var _this = this;
            console.log(this.searchMap);
            this.searchMap.pageNo = parseInt(this.searchMap.pageNo);//转换为数字
            axios.post("/itemSearch/search.do", this.searchMap)
                .then(function (response) {
                    _this.resultMap = response.data;
                }).catch(function (reason) {
                console.log(reason.data);
            });
        },
        pageHandler: function (current) {
            this.searchMap.pageNo = current;
            this.search();
        },
        changeGoPage(num) {
            this.goPage = parseInt(num)
        },
        jump: function (num) {
            console.log(num)
            this.searchMap.pageNo = num;
            this.search();
        }

    },

    created: function () {//创建对象时调用

    },
    mounted: function () {//页面加载完
        let sc = this.getQueryString("sc");
        this.searchMap.keywords = sc
        this.search();
    }
});