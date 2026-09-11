/*
 * MongoDB migration runner:
 *   mongosh "mongodb://localhost:27017/db_btgpactual" \
 *     --file src/main/resources/db/migrations/V1__create_btg_orders.js
 *
 * The migration is intentionally idempotent. It can be executed again safely
 * after a deployment or when a new environment is provisioned.
 */

const migrationId = "V1__create_btg_orders";
const collectionName = "tb_btg_orders";

const validator = {
  $jsonSchema: {
    bsonType: "object",
    required: ["orderId", "customer", "totalValue", "createdAt"],
    properties: {
      _id: { bsonType: "objectId" },
      orderId: { bsonType: "string", minLength: 1 },
      customer: {
        bsonType: "object",
        required: ["customerId"],
        properties: {
          customerId: { bsonType: "string", minLength: 1 },
          createdAt: { bsonType: "date" }
        }
      },
      totalValue: {
        bsonType: ["decimal", "double", "int", "long"],
        minimum: 0.01
      },
      createdAt: { bsonType: "date" },
      updateAt: { bsonType: "date" },
      items: {
        bsonType: "array",
        items: {
          bsonType: "object",
          required: ["item", "product", "quantity", "price", "totalItemValue"],
          properties: {
            orderItemId: { bsonType: "string" },
            item: { bsonType: ["int", "long"], minimum: 0 },
            product: { bsonType: "string", minLength: 1 },
            quantity: { bsonType: ["int", "long"], minimum: 1 },
            price: {
              bsonType: ["decimal", "double", "int", "long"],
              minimum: 0.01
            },
            totalItemValue: {
              bsonType: ["decimal", "double", "int", "long"],
              minimum: 0.01
            },
            active: { bsonType: ["bool", "null"] },
            createdAt: { bsonType: "date" }
          }
        }
      }
    }
  }
};

const migrationCollection = db.getCollection("schema_migrations");
const alreadyApplied = migrationCollection.findOne({ _id: migrationId });

if (!alreadyApplied) {
  if (!db.getCollectionNames().includes(collectionName)) {
    db.createCollection(collectionName, {
      validator,
      validationLevel: "strict",
      validationAction: "error"
    });
  } else {
    db.runCommand({
      collMod: collectionName,
      validator,
      validationLevel: "strict",
      validationAction: "error"
    });
  }

  const orders = db.getCollection(collectionName);
  // Keep MongoDB's generated names aligned with MongoIndexConfig, so the
  // application startup remains idempotent after this migration.
  orders.createIndex({ createdAt: 1 });
  orders.createIndex({ orderId: 1 }, { unique: true });
  orders.createIndex({ createdAt: 1, totalValue: -1 });

  migrationCollection.insertOne({
    _id: migrationId,
    appliedAt: new Date(),
    description: "Create and validate BTG orders collection"
  });
}
