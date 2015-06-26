var Interest = Backbone.Model.extend({
    defaults: {},
    initialize: function () {
        console.log("initing");
        this.on("change", function () {
            console.log("changing");
        });
    }
});

var myInterest = new Interest({});


var InterestView = Backbone.View.extend({

    events: {
        'click [type="radio"]': 'recordInterest'
    },

    recordInterest: function () {
        console.log("recording interest");
        console.log(this);
    }

})

var InterestView = new InterestView({el: $('.interests')});

