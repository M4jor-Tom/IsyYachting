import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ReservationService from '@/entities/reservation/reservation.service';
import { IReservation } from '@/shared/model/reservation.model';

import { IDay, Day } from '@/shared/model/day.model';
import DayService from './day.service';
import { CancelingWeather } from '@/shared/model/enumerations/canceling-weather.model';

const validations: any = {
  day: {
    time: {
      required,
    },
    forecastedWeather: {},
  },
};

@Component({
  validations,
})
export default class DayUpdate extends Vue {
  @Inject('dayService') private dayService: () => DayService;
  @Inject('alertService') private alertService: () => AlertService;

  public day: IDay = new Day();

  @Inject('reservationService') private reservationService: () => ReservationService;

  public reservations: IReservation[] = [];
  public cancelingWeatherValues: string[] = Object.keys(CancelingWeather);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dayId) {
        vm.retrieveDay(to.params.dayId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.day.id) {
      this.dayService()
        .update(this.day)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.day.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.dayService()
        .create(this.day)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.day.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.day[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.day[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.day[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.day[field] = null;
    }
  }

  public retrieveDay(dayId): void {
    this.dayService()
      .find(dayId)
      .then(res => {
        res.time = new Date(res.time);
        this.day = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.reservationService()
      .retrieve()
      .then(res => {
        this.reservations = res.data;
      });
  }
}
