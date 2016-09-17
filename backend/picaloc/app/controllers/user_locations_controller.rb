class UserLocationsController < ApplicationController

  def index
    render :json => UserLocation.all
  end

  # create or update user locations
  def create
    if validate_token
      user_location = UserLocation.find_by_user_id(@payload['user_id'])
      if user_location.nil?
        new_params = user_location_params
        new_params[:user_id] = @payload['user_id']
        @user_location = UserLocation.new(new_params)
        if @user_location.save
          render json: {status: :created}
        else
          render json: @user_location.errors, status: :unprocessable_entity
        end
      else
        if user_location.update(user_location_params)
          render json: {status: :updated}
        else
          render json: {status: :failed}
        end
      end
    end
  end

  private
  def user_location_params
    params.permit(:user_id, :longitude, :latitude)
  end
end
