/**
 * This file contains project specific customizations for the webpack build. 
 * It is autogenerated if it didn't exist or if it was made for an older
 * incompatible version.
 *
 * Defaults are provided in an autogenerated webpack.generated.js file and used by this file. 
 * The webpack.generated.js file is overwritten on each build and no customization can be done there.
 */
const merge = require('webpack-merge');
const flowDefaults = require('./webpack.generated.js');

module.exports = merge(flowDefaults, {
    stats: {
        logging: 'verbose'
    },
    infrastructureLogging: {
        level: 'verbose'
    }
    /*,
    watchOptions: {
        aggregateTimeout: 5
    }
    */
});

/**
 * This file can be used to configure the flow plugin defaults.
 * <code>
 *   // Add a custom plugin
 *   flowDefaults.plugins.push(new MyPlugin());
 *
 *   // Update the rules to also transpile `.mjs` files
 *   if (!flowDefaults.module.rules[0].test) {
 *     throw "Unexpected structure in generated webpack config";
 *   }
 *   flowDefaults.module.rules[0].test = /\.m?js$/
 *
 *   // Include a custom JS in the entry point in addition to generated-flow-imports.js
 *   if (typeof flowDefaults.entry.index != "string") {
 *     throw "Unexpected structure in generated webpack config";
 *   }
 *   flowDefaults.entry.index = [flowDefaults.entry.index, "myCustomFile.js"];
 * </code>
 * or add new configuration in the merge block.
 * <code>
 *   module.exports = merge(flowDefaults, {
 *     mode: 'development',
 *     devtool: 'inline-source-map'
 *   });
 * </code>
 */
