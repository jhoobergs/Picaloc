Rails.application.routes.draw do
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  resources :posts
  resources :user_locations
  resources :likes

  post 'posts/add', to: 'posts#create'
  post 'posts/get', to: 'posts#index'
  patch 'posts/:id', to: 'posts#update'
end
