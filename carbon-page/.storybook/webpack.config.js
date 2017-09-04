const path = require('path');
module.exports = {
  plugins: [
    // your custom plugins
  ],
  module: {
    loaders: [
      {
        // static-css
        test: /\.css$/,
        loaders: ['style', 'css'],
        include: path.resolve(__dirname, '../public/css')
      },
      {
        // static-css
        test: /\.css$/,
        loaders: ['style', 'css'],
        include: path.resolve(__dirname, '../node_modules/')
      },
      {
        // src-css
        test: /\.css$/,
        loaders: ['style', 'css?modules'],
        include: [
          path.resolve(__dirname, '../src'),
        ]
      },
      {
        // resources
        test: /\.(jpg|png|svg|gif)$/,
        loader: 'file?name=assets/[hash].[ext]',
        include: path.resolve(__dirname, '../src'),
      },
      {
        test: /\.(otf|eot|svg|ttf|woff|woff2)(\?.+)?$/,
        loader: 'url',
        include: path.resolve(__dirname, '../public/vendor')
      }
    ],
  },
  resolve: {
    alias: {
    },
  },
};
