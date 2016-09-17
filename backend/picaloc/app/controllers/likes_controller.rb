class LikesController < ApplicationController
  def index
    render :json => Like.all
  end

  def create
    if validate_token
      new_params = like_params
      new_params[:user_id] = @payload['user_id']
      @like = Like.new(new_params)
      if @like.save
        render json: {status: :created}
      else
        render json: @post.errors, status: :unprocessable_entity
      end
    end
  end

  def destroy
    @like.destroy
    head :no_content
  end

  private
  def like_params
    params.permit(:user_id, :post_id)
  end
end
