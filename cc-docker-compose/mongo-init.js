db.createUser(
    {
        user:"cxnMongo",
        pwd:"ccMongoPass",
        customData:
            {
                data:"anydata"
            },
        roles : [
            {
                role: "clusterAdmin",
                db: "admin"
            },
            {
                role: "readAnyDatabase",
                db: "admin"
            },
            "readWrite"
        ]
    }
)
