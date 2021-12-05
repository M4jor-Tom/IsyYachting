import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IReservation } from '@/shared/model/reservation.model';

import ReservationService from './reservation.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Reservation extends Vue {
  @Inject('reservationService') private reservationService: () => ReservationService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public reservations: IReservation[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllReservations();
  }

  public clear(): void {
    this.retrieveAllReservations();
  }

  public retrieveAllReservations(): void {
    this.isFetching = true;
    this.reservationService()
      .retrieve()
      .then(
        res => {
          this.reservations = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IReservation): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeReservation(): void {
    this.reservationService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('isyYachtingApp.reservation.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllReservations();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
