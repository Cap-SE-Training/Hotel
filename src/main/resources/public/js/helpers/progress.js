function Progress(options) {
    var self = this;

    _.extend(self, options);

    self.nextButton.on('click', function() {
        self.next();
        self.check();
    });
    self.previousButton.on('click', function() {
        self.previous();
    });
}

_.extend(Progress.prototype, {
    setSteps: function(steps) {
        var self = this;
        self.steps = steps;
        self.currentStep = 1;

        _.each(steps, function (step, index) {
            var stepNumber = index + 1;
            var bar = $('<span class="bar"></span>');
            var circle = $('<div class="step' + stepNumber + ' circle"></div>');
            if (stepNumber === self.currentStep) {
                $(circle).addClass('active');
            }
            var circleLabel = $('<span class="label">' + stepNumber + '</span>');
            var circleTitle = $('<span class="title"> ' + step.name + ' </span>');

            circle.append(circleLabel).append(circleTitle);
            self.container.append(circle);
            if (steps.length !== stepNumber) {
                self.container.append(bar);
            }
        });

        self.init();
    },
    check: function () {
        if (this.steps[this.currentStep-1].check()) {
            this.nextButton.prop("disabled", false);
        } else {
            this.nextButton.attr('disabled', true);
        }
    },
    init: function () {
        this.steps[this.currentStep - 1].container.show();
        this.steps[this.currentStep - 1].init();
    },
    next: function() {
        if (this.currentStep === this.steps.length) {
            this.finish();
            return;
        }

        this.steps[this.currentStep - 1].container.hide();
        this.currentStep++;
        this.steps[this.currentStep - 1].container.show();
        this.steps[this.currentStep - 1].init();
        this.setActive();
    },
    previous: function() {

    },
    setActive: function() {
        var circles = $('.prog .circle');
        circles.removeClass('done');
        circles.removeClass('active');
        for (var i = 0; i < this.currentStep; i++) {
            $('.prog .circle.step' + i).addClass('done');
        }
        $('.prog .circle.step' + this.currentStep).addClass('active');
        if (this.currentStep === this.steps.length) {
            this.nextButton.html('Create!');
        } else {
            this.nextButton.html('Next');
        }
    },
    setCurrentStep: function(step, init) {
        init = init || false;

        this.currentStep = step;
        _.each(this.steps, function(step) {
            step.container.hide();
        });
        this.steps[this.currentStep - 1].container.show();
        if (init) {
            this.steps[this.currentStep - 1].init();
        }
        this.setActive();
    }
});