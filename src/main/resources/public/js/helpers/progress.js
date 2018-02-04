function Progress(container, previousButton, nextButton, steps) {
    var self = this;

    self.container = container;
    self.previousButton = previousButton;
    self.nextButton = nextButton;

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
        this.steps[this.currentStep - 1].container.hide();
        this.currentStep++;
        this.steps[this.currentStep - 1].container.show();
        this.steps[this.currentStep - 1].init();
    },
    previous: function() {

    }
});