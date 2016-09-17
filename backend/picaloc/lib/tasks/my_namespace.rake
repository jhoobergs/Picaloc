namespace :my_namespace do
  desc 'TODO'
  task reset_like_counters: :environment do
    Post.find_each { |post| Post.reset_counters(post.id, :likes) }
  end

end
