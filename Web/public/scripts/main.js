var ThumbnailBox = React.createClass({
    loadDataFromServer: function() {
        $.ajax({
            url: '',
            dataType: 'json',
            cache: false,
            success: function(data) {
                this.setState({data: data});
            }.bind(this),
            error: function(xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },

    getInitialState: function() {
        return {data: []};
    },

    render: function() {
        return (
            <div className="thumbnailBox">
                <h1>Taken here</h1>
                <ThumbnailList data={this.state.data}/>
            </div>
        );
    }
});

var ThumbnailList = React.createClass({
    render: function() {
        return (
            <div className="thumbnailList">
                <Thumbnail author="Someone">Hi</Thumbnail>
                <Thumbnail author="Someone else">Hi again</Thumbnail>
            </div>
        );
    }
});

var Thumbnail = React.createClass({
    render: function() {
        return (
            <div className="thumb">
                <h2 className="commentAuthor">{this.props.author}</h2>
                {this.props.children}
            </div>
        )
    }
});

ReactDOM.render(
    <ThumbnailBox />,
    document.getElementById('thumbnails')
);