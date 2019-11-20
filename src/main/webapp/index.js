/**
 * Online UML editor made up of PlanUML , Orion and Closure.
 * 
 * @author lxb
 * 
 */
App = function() {

    this.init = function(parent, $el) {
        this.$el = $el;
        this.form = $el.find("form").get(0)
        $el.on('click.toolbar.item', '#submit', $.proxy(this.submit, this));
    }

    this.submit = function() {
        var data = new FormData();
        var file_ = this.form.mapping_file
        data.append(file_.name, file_.files[0])
        var f = this.form.stacktrace
        data.append(f.name, f.value)
        $.ajax({
            url : 'retrace',
            method : 'POST',
            data : new FormData(this.form),
            processData: false,
            contentType: false
        }).then((text,b)=>{
            $('#transcoded').text(text);
        });
    }

}

$(function() {
    var ins = new App();
    ins.init(null, $('body'), null);
})