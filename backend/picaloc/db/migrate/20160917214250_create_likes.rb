class CreateLikes < ActiveRecord::Migration[5.0]
  def change
    create_table :likes do |t|
      t.string :user_id

      t.timestamps
    end

    add_reference :likes, :post, index: true, foreign_key: true
  end
end
