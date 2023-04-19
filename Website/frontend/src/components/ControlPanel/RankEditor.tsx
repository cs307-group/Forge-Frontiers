import {AnimatePresence, motion} from "framer-motion";
import {useState} from "react";
import Stripe from "stripe";

import {Button} from "../Button";
import {BaseInput} from "../Input/BaseInput";
import {Switch} from "../Switch";

type StripeProduct = Stripe.Product & {default_price: Stripe.Price};

const AP: any = AnimatePresence;

export function RankEditor({
  products,
}: {
  products: (Stripe.Product & {default_price: Stripe.Price})[];
}) {
  const [isAddingItem, setIsAddingItem] = useState(false);
  const [editingItem, setEditingItem] = useState<StripeProduct | null>(null);
  function edit(item: StripeProduct | null) {
    setEditingItem(item);
    setIsAddingItem(true);
  }
  function closeModal() {
    setEditingItem(null);
    setIsAddingItem(false);
  }
  return (
    <div>
      <div className="grid-cols-3 gap-4 sm:grid">
        {products.map((product) => (
          <div
            key={product.id}
            className="flex max-w-sm items-center overflow-hidden rounded-lg p-2 shadow-lg"
          >
            <div className="w-full px-6 py-4">
              <div className="mb-2 text-xl font-bold">
                {product.name} {!product.active && "(inactive)"}
              </div>
              <p className="text-sm">{product.description}</p>
              <p className="text-bold mt-2 text-base">
                ${product.default_price?.unit_amount! / 100}
              </p>
            </div>
            <button
              className="font-bold underline underline-offset-2"
              onClick={() => edit(product)}
            >
              Edit
            </button>
          </div>
        ))}

        {isAddingItem && (
          <motion.div
            initial={{opacity: 0}}
            animate={{opacity: 1}}
            exit={{opacity: 0, pointerEvents: "none"}}
            className="fixed bottom-0 left-0 right-0 top-0 flex items-center justify-center bg-black bg-opacity-50"
            onClick={closeModal}
          >
            <motion.div
              initial={{scale: 0}}
              animate={{scale: 1}}
              exit={{scale: 0}}
              className="w-[80%] max-w-[600px] rounded-xl bg-white p-5 text-black shadow-lg"
              onClick={(e) => e.stopPropagation()}
            >
              <h2 className="mb-2 text-lg font-bold">
                {editingItem ? (
                  <>Product ID: {editingItem!.id}</>
                ) : (
                  <>Add new Product</>
                )}
              </h2>
              <EditProduct product={editingItem!} />
            </motion.div>
          </motion.div>
        )}
      </div>
      <div className="mt-4 flex items-center justify-center">
        <Button className="p-2" onClick={() => edit(null)}>
          Add Rank
        </Button>
      </div>
    </div>
  );
}

function EditProduct({product}: {product?: StripeProduct}) {
  return (
    <div>
      <form action="/api/edit-product" method="post">
        <input type="hidden" name="product_id" value={product?.id} />
        <BaseInput
          dark={false}
          name="name"
          required
          label="name"
          // value={name}
          defaultValue={product?.name}
        />
        <label className="flex flex-col">
          <span>Description</span>
          <textarea
            className="border-2 border-[#e3e3e3] p-2"
            name="description"
            defaultValue={product?.description!}
          ></textarea>
        </label>
        <BaseInput
          dark={false}
          name="price"
          label="price"
          defaultValue={product ? product.default_price.unit_amount! / 100 : 0}
        />
        {product && (
          <span className="flex items-center mt-2 gap-4">
            Active: <Switch defaultChecked={product.active} name="active" />
          </span>
        )}
        <div className="flex items-center justify-end">
          <Button className="mt-2 p-2">Submit</Button>
        </div>
      </form>
    </div>
  );
}
