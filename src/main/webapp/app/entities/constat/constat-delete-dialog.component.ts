import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Constat } from './constat.model';
import { ConstatPopupService } from './constat-popup.service';
import { ConstatService } from './constat.service';

@Component({
    selector: 'jhi-constat-delete-dialog',
    templateUrl: './constat-delete-dialog.component.html'
})
export class ConstatDeleteDialogComponent {

    constat: Constat;

    constructor(
        private constatService: ConstatService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.constatService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'constatListModification',
                content: 'Deleted an constat'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-constat-delete-popup',
    template: ''
})
export class ConstatDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private constatPopupService: ConstatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.constatPopupService
                .open(ConstatDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
