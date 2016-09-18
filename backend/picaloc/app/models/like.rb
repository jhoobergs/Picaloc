class Like < ApplicationRecord
  belongs_to :post, :counter_cache => true
  validates :user_id, uniqueness: { scope: :post_id }
end
