module.exports = {
    entry: {
        'message/room': './pages/message/room.js',
    },
    output: {
        path: __dirname + '/../../resources/static/js',
        filename: '[name].js',
        publicPath: '/public/'
    },
    module: {
        loaders: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015']
                }
            }
        ]
    },
    devtool: 'source-map',
    resolve: {
        extensions: ['', '.js']
    }
};