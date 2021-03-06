var fs = require("fs");
var path = require("path");
var logger = require("./logger");

var default_path = path.join(__dirname + '/../config.json');
var default_config = {};

default_config.server_port = 3000;
default_config.backend_server = "localhost";
default_config.backend_port = 8888;

/**
 * Constructor for a Config object.
 * @constructor
 */
var Config = function() {
    // Creation of the object loads the values stored in the config.
    this.config = this.load();
};

/**
 * Validates the config file keys. If one of the default keys is missing or there is a syntax error, it returns false.
 * @param config   Config object.
 * @returns {boolean}
 */
Config.prototype.validate = function (config) {
    var default_keys = Object.keys(default_config);
    var config_keys = Object.keys(config);
    var i, key;

    // Check if config contains all default keys.
    for (i = 0; i < default_keys.length; i++) {
        key = default_keys[i];
        if (config_keys.indexOf(key) === -1) {
            return false;
        }
    }

    // Check if the config only contains default keys.
    for (i = 0; i < config_keys.length; i++) {
        key = config_keys[i];
        if (default_keys.indexOf(key) === -1) {
            return false;
        }
    }

    return true;
};

/**
 * Loads the config from file, if not existent a new file is created and the default is returned.
 * @param config_path       Path to the config file.
 * @returns {object}
 */
Config.prototype.load = function (config_path) {
    // If no path specified, use the default.
    if (config_path === undefined) {
        config_path = default_path;
    }

    var config;
    if (this.exists(config_path)) {
        try {
            logger.logMessage(logger.levels.INFO, "Reading config file from " + config_path);
            config = JSON.parse(fs.readFileSync(config_path));
        } catch (e) {
            logger.logMessage(logger.levels.ERROR, "Config file could not be read from " + config_path + " , using default now.");
            config = default_config;
        }
    } else {
        try {
            fs.writeFileSync(config_path, JSON.stringify(default_config));
            logger.logMessage(logger.levels.INFO, "Successfully created default log file in " + config_path);
            config = default_config;
        } catch (e) {
            logger.logMessage(logger.levels.ERROR, "Could not write default config file file at " + config_path + " , using default now.");
            config = default_config;
        }
    }

    if (this.validate(config) === true) {
        logger.logMessage(logger.levels.INFO, "Config has been loaded successfully.");
        return config;
    } else {
        logger.logMessage(logger.levels.WARNING, "The config file is invalid, loading default.");
        return default_config;
    }
};

/**
 * Checks if a file exists.
 * @param config_path   Path to the file to check.
 * @returns {boolean}
 */
Config.prototype.exists = function (config_path) {
    try {
        fs.statSync(config_path);
        return true;
    } catch (e) {
        return false;
    }
};

/**
 * Returns the config JSON.
 * @param key     The key to fetch.
 * @returns {Object|*}
 */
Config.prototype.get = function (key) {
    return this.config[key];
};

/**
 * Returns the default config.
 * @returns {Object}
 */
Config.prototype.getDefaultConfig = function () {
    return default_config;
};

module.exports = new Config();