export const properties_set = {
    prod: {
        env: "prod"
    },
    local: {
        env: "local",
        serverUrl: "http://localhost:8080/mc"
    }
};

const env = process.env.REACT_ENV;
console.log(process.env)
var local_properties;
if (env === "prod") {
    local_properties = properties_set.prod;
} else {
    local_properties = properties_set.local;
}

export const properties = local_properties;