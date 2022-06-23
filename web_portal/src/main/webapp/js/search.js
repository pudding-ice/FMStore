new Vue({
    el: "#app",
    data: {
        searchMap: {
            'keywords': '',//搜索关键字
            'category': '',//分类
            'brand': '',//品牌
            'spec': {},//规格
            'price': '',//价格
            'pageNo': 1,//当前页
            'pageSize': 40,//每页展示多少条数据
            'sort': '',//排序
            'sortField': ''//排序的字段
        }
    },
    methods: {
        getQueryString: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return (decodeURI(r[2]));
            }
            return null;
        }
    },
    watch: { //监听属性的变化

    },
    created: function () {//创建对象时调用

    },
    mounted: function () {//页面加载完
        let sc = this.getQueryString("sc");
        alert(sc);
    }
});