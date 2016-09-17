class CreateUserLocations < ActiveRecord::Migration[5.0]
  def change
    create_table :user_locations do |t|
      t.string :user_id
      t.float :longitude
      t.float :latitude

      t.timestamps
    end
  end
end
